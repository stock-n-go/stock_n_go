import usePortfolioChartQuery from "@api/portfolio/queries/usePortfolioChartQuery";
import Legend from "@components/common/PieChart/Legend";
import RechartPieChart from "@components/common/PieChart/RechartPieChart";
import { CSSProperties } from "react";
import { colorPalette } from "styles/ColorPalette";

type Props = {
  width: number;
  height: number;
  legendStyle?: CSSProperties;
};

export default function PortfolioPieChart({
  width,
  height,
  legendStyle,
}: Props) {
  const { data: pieData } = usePortfolioChartQuery();

  const coloredPieData = pieData?.map((item, index) => ({
    ...item,
    fill: colorPalette[index],
  }));

  return coloredPieData ? (
    <>
      <RechartPieChart width={width} height={height} pieData={coloredPieData} />
      <Legend style={legendStyle} pieData={coloredPieData} />
    </>
  ) : (
    <div>로딩중</div>
    // TODO: loading indicator
  );
}
