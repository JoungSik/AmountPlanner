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
import { UserType } from 'src/models/UserType';
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
                        <Heading fontSize={'2xl'}>로그인</Heading>
                        <FormControl id="email"
                                     isInvalid={errors.email?.type === 'required' || errors.email?.type === 'pattern'}>
                            <FormLabel>이메일</FormLabel>
                            <Input type="email" {...register('email', {
                                required: true,
                                pattern: /^\w+@[a-zA-Z_]+?\.[a-zA-Z]*$/i,
                            })} />
                            <FormErrorMessage>{errors.email?.type === 'required' ? '이메일을 입력해주세요.' : '이메일 형식을 지켜주세요.'}</FormErrorMessage>
                        </FormControl>
                        <FormControl id="password" isInvalid={errors.password?.type === 'required'}>
                            <FormLabel>비밀번호</FormLabel>
                            <Input type="password" {...register('password', { required: true })} />
                            <FormErrorMessage>{errors.password && '비밀번호를 입력해주세요.'}</FormErrorMessage>
                        </FormControl>
                        <Stack spacing={6}>
                            <Button type={'submit'} colorScheme={'blue'} variant={'solid'}
                                    isLoading={mutation.isLoading}>로그인</Button>
                            <Button type={'button'} variant={'solid'} onClick={onRegister}>회원가입</Button>
                        </Stack>
                    </Stack>
                </Flex>
            </Stack>
            <AlertDialog motionPreset="slideInBottom" onClose={onClose} isOpen={isOpen} isCentered
                         leastDestructiveRef={cancelRef}>
                <AlertDialogOverlay />
                <AlertDialogContent>
                    <AlertDialogHeader>로그인 실패</AlertDialogHeader>
                    <AlertDialogCloseButton />
                    <AlertDialogBody>이메일과 비밀번호를 다시 확인해주세요.</AlertDialogBody>
                    <AlertDialogFooter>
                        <Button ref={cancelRef} onClick={onClose}>확인</Button>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </>
    );
};

export default Login;
