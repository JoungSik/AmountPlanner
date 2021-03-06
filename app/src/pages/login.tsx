import React, { useEffect, useRef } from 'react';
import { useHistory } from 'react-router-dom';
import {
    AlertDialog,
    AlertDialogBody,
    AlertDialogCloseButton,
    AlertDialogContent,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogOverlay,
    Button,
    Flex,
    FormControl,
    FormErrorMessage,
    FormLabel,
    Heading,
    Input,
    Stack,
    useDisclosure,
} from '@chakra-ui/react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { useMutation, useQueryClient } from 'react-query';

import { User } from 'src/api';
import { UserType } from 'src/models/user_type';
import useLocalStorage from 'src/utils/storage';

const Login = () => {
    const history = useHistory();
    const { storedValue, setStoredValue } = useLocalStorage('user');
    const { isOpen, onOpen, onClose } = useDisclosure();
    const cancelRef = useRef<HTMLButtonElement>(null);

    const { register, handleSubmit, formState: { errors } } = useForm<UserType>();
    const onRegister = () => history.push('/register');
    const onSubmit: SubmitHandler<UserType> = data => mutation.mutate(data);

    const queryClient = useQueryClient();
    const mutation = useMutation(
        userinfo => User.login({ email: userinfo.email, password: userinfo.password }), {
            onMutate: async (user: UserType) => {
                const previousTodos = queryClient.getQueryData<UserType>('user');
                return { previousTodos };
            },
            onSuccess: (response, variables, context) => {
                const user = {
                    ...response.data,
                    authorization: response.headers.authorization,
                };

                setStoredValue(user);
                queryClient.setQueryData<UserType>('user', user);
            },
            onError: () => {
                onOpen();
            },
            onSettled: () => {
                queryClient.invalidateQueries('user');
            },
        });

    useEffect(() => {
        if (storedValue) {
            history.replace('/');
        }
    }, [history, storedValue]);

    return (
        <>
            <Stack direction={{ base: 'column', md: 'row' }}>
                <Flex p={8} flex={1} align={'center'} justify={'center'}>
                    <Stack as={'form'} spacing={4} w={'full'} maxW={'md'} onSubmit={handleSubmit(onSubmit)}>
                        <Heading fontSize={'2xl'}>?????????</Heading>
                        <FormControl id="email"
                                     isInvalid={errors.email?.type === 'required' || errors.email?.type === 'pattern'}>
                            <FormLabel>?????????</FormLabel>
                            <Input type="email" {...register('email', {
                                required: true,
                                pattern: /^\w+@[a-zA-Z_]+?\.[a-zA-Z]*$/i,
                            })} />
                            <FormErrorMessage>{errors.email?.type === 'required' ? '???????????? ??????????????????.' : '????????? ????????? ???????????????.'}</FormErrorMessage>
                        </FormControl>
                        <FormControl id="password" isInvalid={errors.password?.type === 'required'}>
                            <FormLabel>????????????</FormLabel>
                            <Input type="password" {...register('password', { required: true })} />
                            <FormErrorMessage>{errors.password && '??????????????? ??????????????????.'}</FormErrorMessage>
                        </FormControl>
                        <Stack spacing={6}>
                            <Button type={'submit'} colorScheme={'blue'} variant={'solid'}
                                    isLoading={mutation.isLoading}>?????????</Button>
                            <Button type={'button'} variant={'solid'} onClick={onRegister}>????????????</Button>
                        </Stack>
                    </Stack>
                </Flex>
            </Stack>
            <AlertDialog motionPreset="slideInBottom" onClose={onClose} isOpen={isOpen} isCentered
                         leastDestructiveRef={cancelRef}>
                <AlertDialogOverlay />
                <AlertDialogContent>
                    <AlertDialogHeader>????????? ??????</AlertDialogHeader>
                    <AlertDialogCloseButton />
                    <AlertDialogBody>???????????? ??????????????? ?????? ??????????????????.</AlertDialogBody>
                    <AlertDialogFooter>
                        <Button ref={cancelRef} onClick={onClose}>??????</Button>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </>
    );
};

export default Login;
