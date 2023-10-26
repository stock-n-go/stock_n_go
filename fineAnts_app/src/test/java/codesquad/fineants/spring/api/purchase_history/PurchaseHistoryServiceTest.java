package codesquad.fineants.spring.api.purchase_history;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import codesquad.fineants.domain.member.Member;
import codesquad.fineants.domain.member.MemberRepository;
import codesquad.fineants.domain.oauth.support.AuthMember;
import codesquad.fineants.domain.portfolio.Portfolio;
import codesquad.fineants.domain.portfolio.PortfolioRepository;
import codesquad.fineants.domain.portfolio_holding.PortFolioHoldingRepository;
import codesquad.fineants.domain.portfolio_holding.PortfolioHolding;
import codesquad.fineants.domain.purchase_history.PurchaseHistoryRepository;
import codesquad.fineants.domain.stock.Market;
import codesquad.fineants.domain.stock.Stock;
import codesquad.fineants.domain.stock.StockRepository;
import codesquad.fineants.spring.api.purchase_history.request.PurchaseHistoryCreateRequest;
import codesquad.fineants.spring.api.purchase_history.response.PurchaseHistoryCreateResponse;

@ActiveProfiles("test")
@SpringBootTest
class PurchaseHistoryServiceTest {

	@Autowired
	private PortFolioHoldingRepository portFolioHoldingRepository;

	@Autowired
	private PortfolioRepository portfolioRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PurchaseHistoryRepository purchaseHistoryRepository;

	@Autowired
	private PurchaseHistoryService service;

	private Member member;

	private Portfolio portfolio;

	private Stock stock;

	private PortfolioHolding portfolioHolding;

	@BeforeEach
	void init() {
		Member member = Member.builder()
			.nickname("일개미1234")
			.email("kim1234@gmail.com")
			.password("kim1234@")
			.provider("local")
			.build();
		this.member = memberRepository.save(member);

		Portfolio portfolio = Portfolio.builder()
			.name("내꿈은 워렌버핏")
			.securitiesFirm("토스")
			.budget(1000000L)
			.targetGain(1500000L)
			.maximumLoss(900000L)
			.member(member)
			.build();
		this.portfolio = portfolioRepository.save(portfolio);

		Stock stock = Stock.builder()
			.companyName("삼성전자보통주")
			.tickerSymbol("005930")
			.companyNameEng("SamsungElectronics")
			.stockCode("KR7005930003")
			.market(Market.KOSPI)
			.build();
		this.stock = stockRepository.save(stock);

		PortfolioHolding portfolioHolding = PortfolioHolding.empty(portfolio, stock);
		this.portfolioHolding = portFolioHoldingRepository.save(portfolioHolding);
	}

	@AfterEach
	void tearDown() {
		purchaseHistoryRepository.deleteAllInBatch();
		portFolioHoldingRepository.deleteAllInBatch();
		portfolioRepository.deleteAllInBatch();
		memberRepository.deleteAllInBatch();
		stockRepository.deleteAllInBatch();
	}

	@DisplayName("사용자는 매입 이력을 추가한다")
	@Test
	void addPurchaseHistory() throws JsonProcessingException {
		// given
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("purchaseDate", LocalDateTime.now());
		requestBody.put("numShares", 3L);
		requestBody.put("purchasePricePerShare", 50000);
		requestBody.put("memo", "첫구매");

		PurchaseHistoryCreateRequest request = objectMapper.readValue(
			objectMapper.writeValueAsString(requestBody), PurchaseHistoryCreateRequest.class);
		Long portfolioHoldingId = portfolioHolding.getId();
		// when
		PurchaseHistoryCreateResponse response = service.addPurchaseHistory(request,
			portfolioHoldingId, AuthMember.from(member));

		// then

		Assertions.assertAll(
			() -> assertThat(response).extracting("id").isNotNull(),
			() -> assertThat(purchaseHistoryRepository.findAll()).hasSize(1)
		);
	}
}
