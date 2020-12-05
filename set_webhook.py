import requests

s = "https://6ce1dbdf37ee.ngrok.io"
url = b"https://api.telegram.org/1332306643:AAFbFC17wUM5Cz-AfBa0vSBz6qhipsUzYbk/setWebhook?".join(s)
res = requests.get(url=url)
print(res.text)
