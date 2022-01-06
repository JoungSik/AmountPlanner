import { requests } from 'src/api';
import { TransactionType } from 'src/models/transaction_type';

const url = 'transactions';

export const Transaction = {
    transactions: async (jwt: string) => await requests.get(url, jwt),
    createTransaction: async (jwt: string, transaction: TransactionType) => await requests.post(url, transaction, jwt),
};