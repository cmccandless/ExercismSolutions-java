class CustomCheckedException extends Exception {

    private static final long serialVersionUID = -7974699643024452706L;

	CustomCheckedException() {
        super();
    }

    CustomCheckedException(String message) {
        super(message);
    }

}
