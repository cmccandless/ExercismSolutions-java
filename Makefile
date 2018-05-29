.PHONY: lint test
lint:
	@echo "No linter configured"

test:
	@ $(foreach FILE,$(FILES), \
		$(call dotest,$(FILE)) \
	)

test-all:
	@ $(foreach FILE,$(shell ls -d */), \
		$(call dotest,$(FILE)) \
	)

define dotest
	# gradle test -Dfile.encoding=utf-8 -p $(1) $(OPTS) || exit 1;
	bash --login -c "gradle test -Dfile.encoding=utf-8 -p $(1) $(OPTS) || exit 1"
endef
