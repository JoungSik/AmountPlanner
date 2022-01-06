import React from 'react';
import { Container, Wrap } from '@chakra-ui/react';
import NewTransactionModal from 'src/components/modals/new_transaction';

const Home = () => {
    return (
        <Container maxW="container.xl">
            <Wrap spacing={4}>
                <NewTransactionModal />
            </Wrap>
        </Container>
    );
};

export default Home;
