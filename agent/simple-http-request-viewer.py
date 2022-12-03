"""
Testowy serwer do śledzenia requestów
Wymagany Python 3.x
Zatrzymanie Ctrl+C
"""
DEFAULT_PORT=8080

from http.server import BaseHTTPRequestHandler, HTTPServer
import logging

class Serwer(BaseHTTPRequestHandler):
    def _set_response(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()
        print()

    def do_GET(self):
        logging.info("GET request: %s\n%s", str(self.path), str(self.headers))
        self.wfile.write("GET request for {}".format(self.path).encode('utf-8'))
        self._set_response()
        print()

    def do_POST(self):
        content_length = int(self.headers['Content-Length'])
        post_data = self.rfile.read(content_length)
        logging.info("POST request: %s\n%s%s\n",
                str(self.path), str(self.headers), post_data.decode('utf-8'))
        self._set_response()
        self.wfile.write("POST request for {}".format(self.path).encode('utf-8'))
        print()

    def do_PUT(self):
        content_length = int(self.headers['Content-Length'])
        post_data = self.rfile.read(content_length)
        logging.info("PUT request: %s\n%s%s\n",
                str(self.path), str(self.headers), post_data.decode('utf-8'))
        self._set_response()
        self.wfile.write("PUT request for {}".format(self.path).encode('utf-8'))
        print()

    def do_DELETE(self):
        logging.info("DELETE request: %s\n%s\n", str(self.path), str(self.headers))
        self._set_response()
        self.wfile.write("DELETE request for {}".format(self.path).encode('utf-8'))
        print()

def run(server_class=HTTPServer, handler_class=Serwer, port=DEFAULT_PORT):
    logging.basicConfig(level=logging.INFO)
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    logging.info('Starting server...\n')
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    httpd.server_close()
    logging.info('Stopping server...\n')

if __name__ == '__main__':
    from sys import argv

    if len(argv) == 2:
        run(port=int(argv[1]))
    else:
        run()
