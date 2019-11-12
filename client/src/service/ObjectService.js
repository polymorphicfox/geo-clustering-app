import axios from 'axios';

export default class ObjectService {
    static getObjects(params) {
        return new Promise(async (resolve, reject) => {
            try {
                const response = await axios.get(process.env.VUE_APP_API_CLUSTERS_ENDPOINT, {params: params});
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
                    response = await axios.post(process.env.VUE_APP_API_CLUSTERS_ENDPOINT, object);
                }
                const data = response.data;
                resolve(data)
            } catch (err) {
                reject(err)
            }
        })
    }
}