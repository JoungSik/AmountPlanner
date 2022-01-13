export const columns = [
    { id: 'date', name: '날짜' },
    { id: 'amount', name: '금액' },
    { id: 'description', name: '설명' },
];

export const ko = {
    search: {
        placeholder: '검색...',
    },
    sort: {
        sortAsc: 'Sort column ascending',
        sortDesc: 'Sort column descending',
    },
    pagination: {
        previous: '이전',
        next: '다음',
        navigate: (page: number, pages: number) => `Page ${page} of ${pages}`,
        page: (page: number) => `Page ${page}`,
        showing: 'Showing',
        of: 'of',
        to: 'to',
        results: 'results',
    },
    loading: 'Loading...',
    noRecordsFound: 'No matching records found',
    error: 'An error happened while fetching the data',
};
