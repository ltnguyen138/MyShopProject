package com.myshop.common.entity.order;

public enum OrderStatus {
	NEW {
		@Override
		public String defaultDescription() {
			return "Đơn hàng đã được tạo bởi khách hàng";
		}
		
	}, 
	
	CANCELLED {
		@Override
		public String defaultDescription() {
			return "Đơn hàng đã bị hủy";
		}
	}, 
	
	PROCESSING {
		@Override
		public String defaultDescription() {
			return "Đơn hàng đang được xử lý";
		}
	},
	
	PACKAGED {
		@Override
		public String defaultDescription() {
			return "Đơn hàng đã được đóng gói";
		}		
	}, 
	
	PICKED {
		@Override
		public String defaultDescription() {
			return "Đơn hàng đã bàn giao cho đơn vị vận chuyển";
		}		
	}, 
	
	SHIPPING {
		@Override
		public String defaultDescription() {
			return "Đơn hàng đang được vận chuyển đến khách hàng";
		}		
	},
	
	DELIVERED {
		@Override
		public String defaultDescription() {
			return "Đơn hàng đã được giao";
		}		
	}, 
	
	RETURN_REQUESTED {
		@Override
		public String defaultDescription() {
			return "Khách hàng yêu cầu trả hàng";
		}		
	},
	
	RETURNED {
		@Override
		public String defaultDescription() {
			return "Khách hàng đã trả hàng";
		}		
	}, 
	

	
	REFUNDED {
		@Override
		public String defaultDescription() {
			return "Khách hàng đã được hoàn tiền";
		}		
	};
	
	public abstract String defaultDescription();
}
