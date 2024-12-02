import os
import pandas as pd

def merge_and_clean_tsv(input_dir, output_file):
    """Merge all .tsv files in the input directory and remove empty rows."""
    
    # List to hold dataframes
    dataframes = []
    
    # Iterate over all files in the input directory
    for file_name in os.listdir(input_dir):
        if file_name.endswith('.tsv'):
            file_path = os.path.join(input_dir, file_name)
            try:
                # Try to read the TSV file into a DataFrame with a specific encoding
                df = pd.read_csv(file_path, sep='\t', encoding='utf-8')
            except UnicodeDecodeError:
                # If utf-8 fails, try a different encoding, e.g., 'ISO-8859-1'
                print(f"UnicodeDecodeError for file {file_path}. Trying 'ISO-8859-1' encoding.")
                df = pd.read_csv(file_path, sep='\t', encoding='ISO-8859-1')
            # Append DataFrame to the list
            dataframes.append(df)
    
    # Concatenate all DataFrames into a single DataFrame
    merged_df = pd.concat(dataframes, ignore_index=True)
    
    # Remove empty rows (rows where all columns are NaN)
    cleaned_df = merged_df.dropna(how='all')
    
    # Remove duplicate rows
    cleaned_df = cleaned_df.drop_duplicates()
    
    # Save the cleaned DataFrame to a new TSV file
    cleaned_df.to_csv(output_file, sep='\t', index=False)
    
    print(f"Combined and cleaned TSV file has been saved to {output_file}")

if __name__ == '__main__':
    input_dir = r"J:/DataJUnit/2Salida"  # Directorio con los archivos .tsv
    output_file = r"J:/DataJUnit/2Salida/final/dataset_total.tsv"  # Archivo de salida
    
    merge_and_clean_tsv(input_dir, output_file)
