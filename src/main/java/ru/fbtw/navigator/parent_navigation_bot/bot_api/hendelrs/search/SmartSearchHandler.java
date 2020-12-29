package ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs.search;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.BotState;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.concurent.ConcurrentItem;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs.InputMessageHandler;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.layout.ButtonLayoutBuilder;
import ru.fbtw.navigator.parent_navigation_bot.cache.UserDataCache;
import ru.fbtw.navigator.parent_navigation_bot.search.MessagePredictorService;
import ru.fbtw.navigator.parent_navigation_bot.search.SearchingService;
import ru.fbtw.navigator.parent_navigation_bot.service.LocaleMessageService;
import ru.fbtw.navigator.parent_navigation_bot.service.ReplyMessagesService;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class SmartSearchHandler implements InputMessageHandler {
	private static final String CONFIRM = "confirm";
	private static final String REJECT = "reject";

	private Map<Integer, String> confirmCallbacks;
	private Map<Integer, String> rejectCallbacks;

	private UserDataCache userDataCache;
	private ReplyMessagesService messagesService;
	private ConcurrentLinkedQueue<ConcurrentItem> queue;
	private MessagePredictorService predictorService;
	private SearchingService searchingService;
	private FutureSearchFinder finder;
	private LocaleMessageService localeMessageService;
	private Map<Integer, SearchItem> searchItemMap;

	public SmartSearchHandler(
			UserDataCache userDataCache,
			ReplyMessagesService messagesService,
			MessagePredictorService predictorService,
			SearchingService searchingService,
			LocaleMessageService localeMessageService
	) {
		this.userDataCache = userDataCache;
		this.messagesService = messagesService;
		this.predictorService = predictorService;
		this.searchingService = searchingService;
		this.localeMessageService = localeMessageService;

		searchItemMap = new HashMap<>();
		confirmCallbacks = new HashMap<>();
		rejectCallbacks = new HashMap<>();
	}

	@Override
	public BotApiMethod<?> handle(Message message, String text) {
		int userId = message.getFrom().getId();
		long chatId = message.getChatId();
		SendMessage replyToUser;
		switch (userDataCache.getUserCurrentBotState(userId)) {
			case SMART_SEARCH:
				List<String> targets = predictorService.splitMessage(text);

				if (targets.size() == 2) {
					SearchItem searchItem = new SearchItem();
					searchItem.setFrom(targets.get(0));
					searchItem.setTo(targets.get(1));
					searchItemMap.put(userId, searchItem);

					String messageFormat = localeMessageService.getMessage("reply.confSearch");
					String messageText = String.format(messageFormat, searchItem.getFrom(), searchItem.getTo());

					replyToUser = messagesService.getReplyUnLocaledMessage(chatId, messageText);
					replyToUser.setReplyMarkup(getInlineMessageButtons(userId));
					userDataCache.setUserCurrentBotState(userId, BotState.CONFIRM_SMART_SEARCH);

				} else {
					replyToUser = sendStatusUpdate(message, "reply.unknown", BotState.SEARCH_GET_FROM);
				}

				if (searchingService.hasName(text)) {
					SendPhoto photo = searchingService.getLevel(text);
					photo.setChatId(chatId);
					queue.add(new ConcurrentItem(photo));
					replyToUser = sendStatusUpdate(message, "reply.searchBegin", BotState.IDLE);
				}
				break;

			case CONFIRM_SMART_SEARCH:
				boolean isConfirm = predictorService.isConfirmMessage(text);

				if (isConfirm) {
					confirmSearch(userId, chatId);
					replyToUser = sendStatusUpdate(message, "reply.searchBegin", BotState.PROCESSING);
				} else {
					replyToUser = sendStatusUpdate(message, "reply.unknown", BotState.SEARCH_GET_FROM);
				}
				break;

			default:
				replyToUser = breakSearch(userId, message);
		}
		return replyToUser;
	}

	private SendMessage breakSearch(int userId, Message message) {
		searchItemMap.remove(userId);
		return sendStatusUpdate(message, "reply.errorInvite", BotState.IDLE);
	}

	private void confirmSearch(int userId, long chatId) {
		SearchItem searchItem = searchItemMap.get(userId);
		beginAsyncSearch(userId, chatId, searchItem.getFrom(), searchItem.getTo());
	}

	@Override
	public BotApiMethod<?> handle(CallbackQuery callbackQuery) {
		int userId = callbackQuery.getFrom().getId();
		long chatId = callbackQuery.getMessage().getChatId();
		String token = callbackQuery.getData();
		BotState botState = userDataCache.getUserCurrentBotState(userId);

		if (confirmCallbacks.remove(userId, token)) {
			rejectCallbacks.remove(userId);
			confirmCallbacks.remove(userId);

			confirmSearch(userId, chatId);
            /*return sendStatusUpdate(
                    callbackQuery.getMessage(), "reply.searchBegin", BotState.PROCESSING);*/
			return sendCallbackResponse(
					"reply.searchBegin",
					false,
					callbackQuery, BotState.PROCESSING
			);
		}

		if (rejectCallbacks.remove(userId, token)) {
			rejectCallbacks.remove(userId);
			confirmCallbacks.remove(userId);
			return sendStatusUpdate(
					callbackQuery.getMessage(), "reply.unknown", BotState.SEARCH_GET_FROM);
           /* return sendCallbackResponse(
                    "reply.errorInvite",
                    false,
                    callbackQuery, BotState.SEARCH_GET_FROM
            );*/
		}

		return sendCallbackResponse(
				"reply.queryError",
				false,
				callbackQuery, botState
		);
	}

	@Override
	public boolean acceptsCallbackQueries() {
		return true;
	}


	public BotApiMethod<?> sendCallbackResponse(
			String replyMessage,
			boolean isAlert,
			CallbackQuery query,
			BotState botState
	) {
		String message = localeMessageService.getMessage(replyMessage);
		int userId = query.getFrom().getId();

		AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
		answerCallbackQuery.setCallbackQueryId(query.getId());
		answerCallbackQuery.setShowAlert(isAlert);
		answerCallbackQuery.setText(message);


		// searchItemMap.remove(userId);
		userDataCache.setUserCurrentBotState(userId, botState);
		return answerCallbackQuery;
	}


	private void beginAsyncSearch(int userId, long chatId, String from, String to) {
		this.finder = new FutureSearchFinder(searchingService, userDataCache, queue);
		finder.setParams(userId, chatId, from, to);
		Thread thread = new Thread(finder);
		thread.setDaemon(true);
		thread.start();
	}

	@Override
	public BotState[] getHandlerName() {
		return new BotState[]{
				BotState.SMART_SEARCH,
				BotState.CONFIRM_SMART_SEARCH
		};
	}

	@Override
	public void setQueue(ConcurrentLinkedQueue<ConcurrentItem> contentQueue) {
		this.queue = contentQueue;
	}

	private SendMessage sendStatusUpdate(Message message, String replyMessage, BotState botState) {
		int userId = message.getFrom().getId();
		long chatId = message.getChatId();
		userDataCache.setUserCurrentBotState(userId, botState);
		return messagesService.getReplyMessage(chatId, replyMessage);
	}

	private InlineKeyboardMarkup getInlineMessageButtons(int userId) {
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

		InlineKeyboardButton yesButton = new InlineKeyboardButton()
				.setText("Да")
				.setCallbackData(nextConfirm(userId));

		InlineKeyboardButton noButton = new InlineKeyboardButton()
				.setText("Нет")
				.setCallbackData(nextReject(userId));

		List<List<InlineKeyboardButton>> layout = new ButtonLayoutBuilder()
				.addRow()
				.addToLastRow(yesButton)
				.addToLastRow(noButton)
				.submitRow()
				.build();

		inlineKeyboardMarkup.setKeyboard(layout);

		return inlineKeyboardMarkup;
	}

	private String nextConfirm(int userId) {
		String token = nextToken();
		confirmCallbacks.put(userId, token);
		return token;
	}

	private String nextReject(int userId) {
		String token = nextToken();
		rejectCallbacks.put(userId, token);
		return token;
	}

	private String nextToken() {
		return UUID.randomUUID().toString();
	}
}
