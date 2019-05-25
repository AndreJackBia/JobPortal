FROM jazzdd/alpine-flask:python3

RUN rm /etc/nginx/nginx.conf
ADD nginx.conf /etc/nginx/nginx.conf

COPY . /app

WORKDIR /app

RUN pip install -r requirements.txt

EXPOSE 80
