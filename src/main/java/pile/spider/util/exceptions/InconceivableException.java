package pile.spider.util.exceptions;

public class InconceivableException extends RuntimeException {

    /**
     * This is a "can't happen" exception.  Rather than swallowing an exception that can't happen,
     * we take the safer approach of throwing an InconceivableException, just in case our hubris
     * caused us to ignore some possibility we just couldn't conceive of at the time.
     *
     * @param reasonThisShouldBeImpossible What might show up in our log.
     * @param cause The underlying exception thrown.
     */
    public InconceivableException(String reasonThisShouldBeImpossible, Throwable cause) {
        super(reasonThisShouldBeImpossible, cause);
    }
}
