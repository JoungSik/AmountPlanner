import React, { useEffect } from 'react';
import {
    Button,
    Flex,
    FormControl,
    FormErrorMessage,
    FormLabel,
    Heading,
    Input,
    Stack,
} from '@chakra-ui/react';
import { useMutation, useQueryClient } from 'react-query';
import { useHistory } from 'react-router-dom';
import { useForm, SubmitHandler } from 'react-hook-form';

import useLocalStorage from 'src/utils/storage';
import { UserType } from 'src/models/UserType';
import { User } from 'src/api';

const Register = () => {
    const history = useHistory();
    const { storedValue, setStoredValue } = useLocalStorage('user');

    const { register, handleSubmit, formState: { errors } } = useForm<UserType>();
    const onSubmit: SubmitHandler<UserType> = data => mutation.mutate(data);

    const queryClient = useQueryClient();
    const mutation = useMutation(
        userinfo => User.register({ email: userinfo.email, password: userinfo.password }), {
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
        <Stack direction={{ base: 'column', md: 'row' }}>
            <Flex p={8} flex={1} align={'center'} justify={'center'}>
                <Stack as={'form'} spacing={4} w={'full'} maxW={'md'} onSubmit={handleSubmit(onSubmit)}>
                    <Heading fontSize={'2xl'}>회원가입</Heading>
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
                                isLoading={mutation.isLoading}>회원가입</Button>
                    </Stack>
                </Stack>
            </Flex>
        </Stack>
    );
};

export default Register;