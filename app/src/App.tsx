import React from 'react';
import { ChakraProvider } from '@chakra-ui/react';
import Routers from 'src/routes';
import theme from 'src/utils/theme';

const App = () => {
    return (
        <ChakraProvider theme={theme}>
            <Routers />
        </ChakraProvider>
    );
};

export default App;
