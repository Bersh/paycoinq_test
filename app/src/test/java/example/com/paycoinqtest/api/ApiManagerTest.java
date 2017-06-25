package example.com.paycoinqtest.api;

import org.easymock.Capture;
import org.junit.Test;

import java.util.List;

import example.com.paycoinqtest.data.RepoInfo;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class ApiManagerTest {
	private int targetId = 1111;
	private String targetName = "abs.io";
	private String mockResponse = "[\n" +
			"{\n" +
			"\"id\": " + targetId + ",\n" +
			"\"name\": \"" + targetName + "\"" +
			"}" +
			"]";

	@Test
	public void parserTest() throws Exception {
		Interceptor mockInterceptor = createNiceMock(Interceptor.class);
		Capture<Interceptor.Chain> chain = new Capture<>();
		Request.Builder requestBuilder = new Request.Builder();
		requestBuilder.get().url("https://api.github.com/users/JakeWharton/repos?page=1&per_page=15");
		expect(mockInterceptor.intercept(capture(chain))).andReturn(new Response.Builder()
				.code(200)
				.message(mockResponse)
				.request(requestBuilder.build())
				.protocol(Protocol.HTTP_1_0)
				.body(ResponseBody.create(MediaType.parse("application/json"), mockResponse.getBytes()))
				.addHeader("content-type", "application/json")
				.build());
		replay(mockInterceptor);

		ApiManager mockApiManager = new ApiManager(mockInterceptor);
		retrofit2.Response<List<RepoInfo>> response = mockApiManager.getReposPage(1);
		List<RepoInfo> repoInfos = response.body();
		assertNotNull(repoInfos);
		assertEquals(1, repoInfos.size());
		RepoInfo repoInfo = repoInfos.get(0);
		assertEquals(targetId, repoInfo.getId());
		assertEquals(targetName, repoInfo.getName());
	}
}