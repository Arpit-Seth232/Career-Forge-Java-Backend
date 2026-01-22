def clean_text(text: str):
    return " ".join(text.replace("\n", " ").split())
