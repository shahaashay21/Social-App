var Sequelize = require('sequelize');

var sequelize = new Sequelize('social_app', 'root', '277', {
	  host: 'localhost',
	  dialect: 'mysql',

	  pool: {
	    max: 5,
	    min: 0,
	    idle: 10000
	  },
	  // pool: false,
});

exports.Users = sequelize.define('users',{
		user_id: {
			type: Sequelize.INTEGER,
			primaryKey: true,
			autoIncrement: true
		},
		user_name: {
			type: Sequelize.STRING,
			allowNull: false,
			unique: true
		},
		email: {
			type: Sequelize.STRING,
			allowNull: false,
			unique: true
		},
		password: {
			type: Sequelize.TEXT,
			allowNull: false
		},
		avatar: {
			type: Sequelize.TEXT,
		},
		verification_code: {
			type: Sequelize.TEXT,
		},
		verified: {
			type: Sequelize.INTEGER,
		},
		city: {
			type: Sequelize.STRING,
		},
		state: {
			type: Sequelize.STRING,
		},
		country: {
			type: Sequelize.STRING,
		},
		profession: {
			type: Sequelize.STRING,
		},
		about_me: {
			type: Sequelize.TEXT,
		},
		interests: {
			type: Sequelize.TEXT,
		},
		privacy: {
			type: Sequelize.INTEGER,
		},
		first_name: {
			type: Sequelize.STRING,
		},
		last_name: {
			type: Sequelize.STRING,
		}
	},{
		timestamps: true,
		freezeTableName: true,
		tableName: 'users'
	}
);

exports.Feed = sequelize.define('feed',{
	feed_id: {
			type: Sequelize.INTEGER,
			primaryKey: true,
			autoIncrement: true,
			allowNull: false
		},
	user_id: {
			type: Sequelize.INTEGER,
			allowNull: false
		},
	image_url: {
		type: Sequelize.TEXT,
		},
	feed_text: {
		type: Sequelize.TEXT,
		}
	},{
		timestamps: true,
		freezeTableName: true,
		tableName: 'feed'
	}
);

exports.Friend = sequelize.define('friend',{
	
	id: {
		type: Sequelize.INTEGER,
		primaryKey: true,
		autoIncrement: true
	},
	user_id: {
			type: Sequelize.INTEGER,
			allowNull: false
		},
	friend_id: {
			type: Sequelize.INTEGER,
			allowNull: false
		},
	request: {
		type: Sequelize.INTEGER,
	},
	follow: {
		type:Sequelize.INTEGER,
	}
	},{
		timestamps: true,
		freezeTableName: true,
		tableName: 'friend'
	}
);

exports.Message = sequelize.define('message',{
	
	id: {
		type: Sequelize.INTEGER,
		primaryKey: true,
		autoIncrement: true
	},
	user_id: {
			type: Sequelize.INTEGER,
			allowNull: false
		},
	rec_id: {
			type: Sequelize.INTEGER,
			allowNull: false
		},
	subject: {
		type: Sequelize.TEXT,
	},
	body: {
		type:Sequelize.TEXT,
	}
	},{
		timestamps: true,
		freezeTableName: true,
		tableName: 'message'
	}
);

exports.sequelize = sequelize;

