import org.junit.Assert.assertEquals

infix fun <T> T.produces(t: T) = assertEquals(t, this)