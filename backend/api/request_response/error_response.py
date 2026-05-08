from typing import List


class ErrorResponse:
    def __init__(self, message: str, invalid_items: List[str] = None):
        self.status = "error"
        self.message = message
        self.invalid_items = invalid_items if invalid_items else []

    def to_dict(self):
        return {
            "status": self.status,
            "message": self.message,
            "error_details": { 
                "invalid_courses": self.invalid_items
            }
        }