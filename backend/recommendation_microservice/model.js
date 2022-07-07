class IModel {
    trainModel() {}
    getTopTen() {}
}

class Model extends IModel {
    constructor() {
      super();
        this.tensors = []; // { postId, score }
    }

    trainModel() {
        const model = await use.load();
        const embeddings1 = (await model.embed([
            "MANGO",
            "mango"
        ])).unstack();

        // 1. fetch all the offer posts

        // const embeddings2 = (await model.embed([
        //     "kiwis are a bird and a fruit, but not at the same time", 
        //     // "Kiwi the bird lives in Austrailia",
        //     // "MANGO",
        //     // "mango",
        //     // "Mango Mango Mango Mango",
        //     // "Mangoes are delicious. I love mongoose.",
        // ])).unstack();
        const embeddings3 = (await model.embed([
            "A yellow succulent piece of juiciness with one large pit and tastes good in smoothies."
        ])).unstack();
        const input = await (await model.embed(["Man go"])).unstack();
        // const number = tf.losses.cosineDistance(embeddings1[0], embeddings1[1]);
        // const variable1 = await tf.losses.cosineDistance(embeddings2[0], embeddings2[1]).array();
        const array = []; 
        
        for (let i = 0; i <= embeddings2.length; i += 1) {
            const embedding = (await model.embed([ value ])).unstack();
            
            const newLocal = await tf.losses.cosineDistance(input, embeddings2[i]).data();
            array.push(newLocal);
        }
        // for (let i = 0; i <= embeddings2.length; i += 1) {
        //     const newLocal = await tf.losses.cosineDistance(input, embeddings2[i]).data();
        //     array.push(newLocal);
        // }
        
        const variable3 = tf.losses.cosineDistance(input[0], embeddings3[0]).toString(true);
        // tf.losses.cosineDistance(embeddings3[0], embeddings3[1], embeddings3[2]).print();


        // console.log("variable1: ", variable1);
        console.log("variable3: ", variable3);
        // console.log("variable3: ", variable3);

        //return percentage similarity
        // res.json((1 - variable2)*100);
        //res.json(Math.max(...array));
    }

    getTopTen() {
        // rank in descending order
        // get top 10 
        // make sure the posts are still upm
        return [];
    }
}
  
module.exports = Model;
