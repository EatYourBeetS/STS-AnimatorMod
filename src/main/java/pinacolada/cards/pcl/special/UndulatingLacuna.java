package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.powers.special.UndulatingLacunaPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class UndulatingLacuna extends PCLCard
{
    public static final PCLCardData DATA = Register(UndulatingLacuna.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetMaxCopies(1);
    public static final int DARK_BONUS = 25;

    public UndulatingLacuna()
    {
        super(DATA);

        Initialize(0, 0, 2, UndulatingLacunaPower.GAIN_PERCENT);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Dark(1);
        SetAffinity_Blue(1);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(DARK_BONUS);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.ChannelOrbs(PCLOrbHelper.Dark, magicNumber);
        PCLActions.Bottom.StackPower(new UndulatingLacunaPower(p, 1));
        SFX.Play(SFX.PCL_PING, 0.4f, 0.4f);
        SFX.Play(SFX.ORB_DARK_CHANNEL, 0.4f, 0.4f);
        PCLGameEffects.Queue.RoomTint(Color.BLACK, 0.8F);
        PCLGameEffects.Queue.Add(new BorderLongFlashEffect(Color.PURPLE));
        PCLActions.Bottom.VFX(VFX.CircularWave(p.hb).SetColors(Color.VIOLET, Color.BLACK).SetScale(0.25f, 5f).SetFrequency(0.1f).SetDuration(1.5f, true));
    }
}