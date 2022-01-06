// thank you, https://gist.github.com/augustolazaro/a7c6fdbf4ec021ecc237aa36ad47202d
import { useCallback, useEffect, useState } from 'react';

const originalSetItem = localStorage.setItem;
localStorage.setItem = function (key, value) {
    const event = new Event('storageChange');
    document.dispatchEvent(event);
    originalSetItem.apply(this, [key, value]);
};
const originalRemoveItem = localStorage.removeItem;
localStorage.removeItem = function (key) {
    const event = new Event('storageChange');
    document.dispatchEvent(event);
    originalRemoveItem.apply(this, [key]);
};

function useLocalStorage(key: string) {
    const storageItem = localStorage.getItem(key);

    const [storedValue, setValue] = useState(
        !!storageItem ? JSON.parse(storageItem) : null,
    );

    const setLocalItem = useCallback(() => {
        /** local storage update is not that fast */
        /** it makes sure that we are getting the new value  */
        setTimeout(() => {
            const itemValueFromStorage = localStorage.getItem(key);
            const value = itemValueFromStorage && JSON.parse(itemValueFromStorage);
            setValue(value);
        }, 50);
    }, [key]);

    const setStoredValue = (value: string | object | null) => {
        switch (typeof value) {
            case 'string':
                localStorage.setItem(key, value);
                break;
            case 'object':
                localStorage.setItem(key, JSON.stringify(value));
                break;
            default:
                localStorage.removeItem(key);
                break;
        }
    };

    useEffect(() => {
        document.addEventListener('storageChange', setLocalItem, false);
        return () => document.removeEventListener('storageChange', setLocalItem);
    }, [setLocalItem]);

    return { storedValue, setStoredValue };
}

export default useLocalStorage;