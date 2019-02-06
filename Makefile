EXTENSION:=java
SOURCE_FILES := $(shell find * -type f -name '*.$(EXTENSION)')
OBJECT_FILES := $(shell echo $(SOURCE_FILES) | tr ' ' '\n' | sed -E s'/src\/(.+)\/java\/(.+)\.java/build\/classes\/\1\/\2.class/g')
EXERCISES := $(shell echo $(SOURCE_FILES) | tr ' ' '\n' | cut -d'/' -f1 | uniq)

.PHONY: lint test $(EXERCISES)
lint:
	@echo "No linter configured"

test-all: $(EXERCISES)

.SECONDEXPANSION:
GET_DEPS = $(filter $@%,$(OBJECT_FILES))
$(EXERCISES): $$(GET_DEPS)

$(OBJECT_FILES): $$(shell echo $$@ | sed -E s'/build\/classes\/(.+)\/(.+)\.class/src\/\1\/java\/\2.java/g')
	bash --login -c "gradle test -Dfile.encoding=utf-8 -p $(shell echo $@ | cut -d'/' -f1) $(OPTS) || exit 1"
