FROM golang:1.9.2

WORKDIR /go/src/app
COPY helloworld.go go.* /go/src/app

RUN go get -d -v app
RUN go install

CMD ["/go/bin/app"]
