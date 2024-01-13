FROM node:21
RUN apt update
RUN git clone https://github.com/mcguinness/saml-sp /usr/src/app
WORKDIR /usr/src/app
RUN npm install
COPY entrypoint.sh entrypoint.sh
EXPOSE 7070
ENTRYPOINT ["./entrypoint.sh"]
CMD ["--help"]
