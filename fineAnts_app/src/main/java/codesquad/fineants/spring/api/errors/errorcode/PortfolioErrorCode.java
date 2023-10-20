package codesquad.fineants.spring.api.errors.errorcode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PortfolioErrorCode implements ErrorCode {

	TARGET_GAIN_LOSS_IS_EQUAL_LESS_THAN_BUDGET(HttpStatus.BAD_REQUEST, "목표 수익금액은 예산보다 커야 합니다"),
	MAXIMUM_LOSS_IS_EQUAL_GREATER_THAN_BUDGET(HttpStatus.BAD_REQUEST, "최대 손실 금액은 예산 보다 작아야 합니다");

	private final HttpStatus httpStatus;
	private final String message;

	@Override
	public String toString() {
		return String.format("%s, %s(name=%s, httpStatus=%s, message=%s)", "포트폴리오 에러 코드",
			this.getClass().getSimpleName(),
			name(),
			httpStatus,
			message);
	}
}