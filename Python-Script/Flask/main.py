from flask import Flask

app = Flask(__name__)

# @ signifies a decorator - way to wrap up a function and modify its behavior!
@app.route('/')
def index():
    return "This is the home page!"

@app.route("/mj")
def mj():
    return "<marquee>MJ</marquee>"

if __name__ == "__main__":
    app.run()
