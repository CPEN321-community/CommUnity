const cartServices = require('../services/cartService');

const addCartController = async (req, res) => {
  const { id } = req.body;
  const response = await cartServices.addToCart(id);
  res.json(response);
};

module.exports = addCartController;
