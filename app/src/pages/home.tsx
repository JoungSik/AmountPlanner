import React, { useEffect, useState } from 'react';
import { Container, Wrap } from '@chakra-ui/react';
import NewTransactionModal from 'src/components/modals/new_transaction';
import { Grid } from 'gridjs-react';
import 'gridjs/dist/theme/mermaid.css';
import useLocalStorage from 'src/utils/storage';
import { useQuery } from 'react-query';
import { Transaction } from 'src/api/transaction';
import { TransactionType } from 'src/models/transaction_type';
import { TCell } from 'gridjs/dist/src/types';
import { columns, ko } from 'src/utils/grid';

const Home = () => {
    const { storedValue } = useLocalStorage('user');
    const { data } = useQuery('transactions', () => Transaction.transactions(storedValue.authorization));

    const [transactions, setTransactions] = useState<{ [key: string]: TCell }[]>([]);

    useEffect(() => {
        if (data) {
            console.log(data);
            setTransactions((data as TransactionType[]).map(d => ({
                    id: d.id,
                    date: d.date,
                    amount: d.amount,
                    description: d.description,
                })),
            );
        }
    }, [data]);

    return (
        <Container maxW="container.xl" my={8}>
            <NewTransactionModal />
            <Grid data={transactions} columns={columns} language={ko}
                  sort={true} resizable={true} search={true} pagination={true} fixedHeader={true} />
        </Container>
    );
};

export default Home;
