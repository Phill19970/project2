package grid.capstone.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.csrf.CsrfToken;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CsrfCookieFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private CsrfCookieFilter csrfCookieFilter;

    @Test
    void testDoFilterInternal() throws Exception {
        // Create a mock CsrfToken
        CsrfToken csrfToken = mock(CsrfToken.class);
        given(csrfToken.getHeaderName()).willReturn("X-CSRF-TOKEN");
        given(csrfToken.getToken()).willReturn("mocked-token");

        // Set the mock CsrfToken as an attribute in the request
        given(request.getAttribute(CsrfToken.class.getName())).willReturn(csrfToken);

        // Call the doFilterInternal method
        csrfCookieFilter.doFilterInternal(request, response, filterChain);

        // Verify that the response header is set with the CsrfToken
        then(response).should().setHeader("X-CSRF-TOKEN", "mocked-token");

        // Verify that the filterChain.doFilter method was called
        then(filterChain).should().doFilter(request, response);
    }

}