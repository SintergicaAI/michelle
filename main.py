import os

from flask import Flask, Response
from flask_cors import CORS

from routes.RepositoryManager import repository_routes

app = Flask(__name__)
CORS(app, support_credentials=True)
app.host = '0.0.0.0'
app.port = 42000

app.register_blueprint(repository_routes)


@app.get("/ping")
def ping():
    return Response(status=200)


if __name__ == '__main__':
    if os.environ.get("GITHUB_TOKEN") is None:
        raise Exception("GITHUB_TOKEN not found")
    app.debug = True
    app.run(app.host, app.port)
