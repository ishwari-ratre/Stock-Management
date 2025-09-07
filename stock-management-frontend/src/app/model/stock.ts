export interface StockEntry {
    symbol: string;
    name: string;
    basePrice: number;
    currentPrice: number;
    industry: string;
    history: HistoryEntry[];
    change: number;
    changePercent: number;
    latestPrice?: number;
    recommendation?: string;
}

export interface HistoryEntry {
    tradeDate: string;
    closePrice: number;
    stock: {
        symbol: string;
        Name: string;
        basePrice: number;
        Industry: string;
    };
}