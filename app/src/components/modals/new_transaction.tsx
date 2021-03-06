import React from 'react';
import {
    Box,
    Button,
    FormControl,
    FormErrorMessage,
    FormLabel,
    Input,
    Modal,
    ModalBody,
    ModalCloseButton,
    ModalContent,
    ModalFooter,
    ModalHeader,
    ModalOverlay,
    useDisclosure,
} from '@chakra-ui/react';
import { useMutation, useQueryClient } from 'react-query';
import { SubmitHandler, useForm } from 'react-hook-form';
import useLocalStorage from 'src/utils/storage';
import { TransactionType } from 'src/models/transaction_type';
import { Transaction } from 'src/api/transaction';

const NewTransactionModal = () => {
    const queryClient = useQueryClient();
    const { storedValue } = useLocalStorage('user');

    const { isOpen, onOpen, onClose } = useDisclosure();

    const { register, handleSubmit, formState: { errors }, reset } = useForm<TransactionType>();

    const mutation = useMutation(
        transaction => Transaction.createTransaction(storedValue.authorization, transaction), {
            onMutate: async (transaction: TransactionType) => {
                await queryClient.cancelQueries('transactions');

                const previousTodos = queryClient.getQueryData<TransactionType[]>('transactions');
                if (previousTodos) {
                    queryClient.setQueryData<TransactionType[]>('transactions', previousTodos.concat(transaction));
                }
                return { previousTodos };
            },
            onSuccess: () => {
                onClose();
                reset();
            },
            onError: (err, variables, context) => {
                if (context?.previousTodos) {
                    queryClient.setQueryData<TransactionType[]>('transactions', context.previousTodos);
                }
            },
            onSettled: () => {
                queryClient.invalidateQueries('transactions');
            },
        },
    );

    const onSubmit: SubmitHandler<TransactionType> = data => mutation.mutate(data);

    return (
        <>
            <Button colorScheme="teal" size="lg" onClick={onOpen}>????????? ??????</Button>

            <Modal isOpen={isOpen} onClose={onClose}>
                <ModalOverlay />
                <ModalContent as={'form'} onSubmit={handleSubmit(onSubmit)}>
                    <ModalHeader>?????? ??????</ModalHeader>
                    <ModalCloseButton />
                    <ModalBody>
                        <FormControl id="date" isInvalid={errors.date?.type === 'required'}>
                            <FormLabel>??????</FormLabel>
                            <Input placeholder="??????" {...register('date', { required: true })} />
                            <FormErrorMessage>{errors.date && '????????? ??????????????????.'}</FormErrorMessage>
                        </FormControl>

                        <FormControl id="amount" isInvalid={errors.amount?.type === 'required'}>
                            <FormLabel>??????</FormLabel>
                            <Input placeholder="??????" {...register('amount', { required: true })} />
                            <FormErrorMessage>{errors.amount && '????????? ??????????????????.'}</FormErrorMessage>
                        </FormControl>

                        <FormControl id="description" mt={2} isInvalid={errors.description?.type === 'required'}>
                            <FormLabel>??????</FormLabel>
                            <Input placeholder="??????" {...register('description', { required: true })} />
                            <FormErrorMessage>{errors.description && '????????? ??????????????????.'}</FormErrorMessage>
                        </FormControl>
                    </ModalBody>
                    <ModalFooter>
                        <Button variant="ghost" mr={3} onClick={onClose}>??????</Button>
                        <Button type={'submit'} colorScheme="blue">??????</Button>
                    </ModalFooter>
                </ModalContent>
            </Modal>
        </>
    );
};

export default NewTransactionModal;
