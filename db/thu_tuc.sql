
IF OBJECT_ID('SP_BANGDIEM') IS NOT NULL
   DROP PROC SP_BANGDIEM
GO
CREATE PROC SP_BANGDIEM(@MAKH VARCHAR(10))
AS BEGIN
   SELECT NGUOIHOC.MANH, HOTEN, DIEM
   FROM NGUOIHOC JOIN HOCVIEN ON NGUOIHOC.MANH = HOCVIEN.MANH
   WHERE MAKH = @MAKH
   ORDER BY DIEM DESC
END
EXEC SP_BANGDIEM 'MKH001'

IF OBJECT_ID('SP_DIEMCHUYENDE') IS NOT NULL
   DROP PROC SP_DIEMCHUYENDE
GO
CREATE PROC SP_DIEMCHUYENDE
AS BEGIN
   SELECT TENCD AS CHUYENDE, COUNT(MAHV) AS SOLUONGHV, MIN(DIEM) AS DIEMTHAPNHAT, MAX(DIEM) AS DIEMCAONHAT, AVG(DIEM) AS DIEMTB
   FROM CHUYENDE JOIN KHOAHOC ON CHUYENDE.MACD = KHOAHOC.MACD
                 JOIN HOCVIEN ON HOCVIEN.MAKH = KHOAHOC.MAKH
   GROUP BY TENCD
END

EXEC SP_DIEMCHUYENDE

IF OBJECT_ID('SP_LUONGNGUOIHOC') IS NOT NULL
   DROP PROC SP_LUONGNGUOIHOC
GO
CREATE PROC SP_LUONGNGUOIHOC
AS BEGIN
   SELECT YEAR(NGAYDK) AS NAM, COUNT(*) AS SOLUONG, MIN(NGAYDK) AS NGAYDT, MAX(NGAYDK) AS NGAYCC
   FROM NGUOIHOC
   GROUP BY YEAR(NGAYDK)
END

EXEC SP_LUONGNGUOIHOC

IF OBJECT_ID('SP_DOANHTHU') IS NOT NULL
   DROP PROC SP_DOANHTHU
GO
CREATE PROC SP_DOANHTHU(@YEAR INT)
AS BEGIN
   SELECT TENCD AS TENCHUYENDE, COUNT( DISTINCT KHOAHOC.MAKH) AS SOKHOAHOC, COUNT(MAHV) AS SOHV, 
   SUM(KHOAHOC.HOCPHI) AS DOANHTHU, MIN(KHOAHOC.HOCPHI) AS HOCPHITHAPNHAT, MAX(KHOAHOC.HOCPHI) AS HOCPHICAONHAT, AVG(KHOAHOC.HOCPHI) AS HOCPHITB
   FROM KHOAHOC JOIN HOCVIEN ON KHOAHOC.MAKH = HOCVIEN.MAKH
                JOIN CHUYENDE ON CHUYENDE.MACD = KHOAHOC.MACD
   WHERE YEAR(NGAYKG) = @YEAR
   GROUP BY TENCD
END

EXEC SP_DOANHTHU 2020

SELECT DISTINCT YEAR(NGAYKG) AS NAM FROM KHOAHOC ORDER BY NAM DESC