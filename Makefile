.PHONY: install uninstall compile build lint test clean

PREFIX ?= /usr/local

test:
	scripts/test.sh

lint:
	scripts/lint.sh

compile:

uninstall:
	$(RM) $(DESTDIR)$(PREFIX)/bin/codacy-analysis-cli
	docker rmi codacy/codacy-analysis-cli:latest

build: compile lint test
	scripts/deploy.sh

clean:
	scripts/clean.sh
