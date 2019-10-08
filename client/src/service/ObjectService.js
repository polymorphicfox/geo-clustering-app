import axios from 'axios';

const url = '/public-api/v3/nearestStores';

export default class ObjectService {
    static getObjects(params) {
        return new Promise(async (resolve, reject) => {
            try {
                const response = await axios.get(url, {params: params});
                const data = response.data;
                resolve(data)
            } catch (err) {
                reject(err)
            }
        })
    }

    static save(object) {
        return new Promise(async (resolve, reject) => {
            try {
                let response;
                if (object.id) {
                    // response = await axios.post(`${url}/${object.id}`, object);
                } else {
                    response = await axios.post(url, object);
                }
                const data = response.data;
                resolve(data)
            } catch (err) {
                reject(err)
            }
        })
    }
}