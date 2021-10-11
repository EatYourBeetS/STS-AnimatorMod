package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.animator.KuragesOathPower;
import eatyourbeets.utilities.GameActions;

public class KokomiSangonomiya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KokomiSangonomiya.class).SetPower(2, CardRarity.RARE).SetMaxCopies(1).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact);

    public KokomiSangonomiya()
    {
        super(DATA);

        Initialize(0, 0, 3, 8);
        SetUpgrade(0, 0, 1, -1);
        SetAffinity_Water(2, 0, 0);
        SetAffinity_Light(1, 1, 0);
        SetAffinity_Earth(1, 0, 0);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
            GameActions.Bottom.VFX(VFX.WaterDome(player.hb.cX,(player.hb.y+player.hb.cY)/2));
            GameActions.Bottom.SFX(SFX.ANIMATOR_WATER_DOME);
            GameActions.Bottom.StackPower(new KuragesOathPower(p, magicNumber, secondaryValue));
    }
}