package dev.centraluniversity.marketplace.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@RequiredArgsConstructor
@Table(name = "reviews")
@Entity
public class Review {


}
