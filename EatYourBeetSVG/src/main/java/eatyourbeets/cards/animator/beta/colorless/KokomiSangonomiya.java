package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.animator.KuragesOathPower;
import eatyourbeets.utilities.GameActions;

public class KokomiSangonomiya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KokomiSangonomiya.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None).SetMaxCopies(1).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact);

    public KokomiSangonomiya()
    {
        super(DATA);

        Initialize(0, 0, 3, 8);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Light(1, 0, 0);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
            GameActions.Bottom.VFX(VFX.WaterDome(player.hb.cX,(player.hb.y+player.hb.cY)/2));
            GameActions.Bottom.SFX(SFX.ANIMATOR_WATER_DOME);
            GameActions.Bottom.StackPower(new KuragesOathPower(p, magicNumber, secondaryValue));
    }
}