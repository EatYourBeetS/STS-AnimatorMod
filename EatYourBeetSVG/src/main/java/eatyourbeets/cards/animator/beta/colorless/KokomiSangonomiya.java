package eatyourbeets.cards.animator.beta.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.OrbCore_Water;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.animator.KuragesOathPower;
import eatyourbeets.utilities.GameActions;

public class KokomiSangonomiya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KokomiSangonomiya.class).SetPower(2, CardRarity.RARE).SetMaxCopies(1).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GenshinImpact).SetMultiformData(2, false);
    public static final EYBCardPreview CORE_PREVIEW = new EYBCardPreview(new OrbCore_Water(), false);

    public KokomiSangonomiya()
    {
        super(DATA);

        Initialize(0, 0, 4, 9);
        SetUpgrade(0,0,0,-1);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Light(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);

        SetEthereal(true);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            SetUpgrade(0,0,0,0);
            SetAutoplay(true);
            this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
        }
        else {
            SetUpgrade(0,0,0,-1);
            SetAutoplay(false);
            this.cardText.OverrideDescription(null, true);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public EYBCardPreview GetCardPreview()
    {
        return upgraded && auxiliaryData.form == 1 ? CORE_PREVIEW : super.GetCardPreview();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
            GameActions.Bottom.VFX(VFX.WaterDome(player.hb.cX,(player.hb.y+player.hb.cY)/2));
            GameActions.Bottom.SFX(SFX.ANIMATOR_WATER_DOME);
            GameActions.Bottom.StackPower(new KuragesOathPower(p, magicNumber, secondaryValue));
            if (upgraded && auxiliaryData.form == 1) {
                AbstractCard c = new OrbCore_Water();
                c.applyPowers();
                c.use(player, null);
            }
    }
}