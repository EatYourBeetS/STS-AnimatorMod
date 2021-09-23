package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KimizugiShiho extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(KimizugiShiho.class).SetAttack(1, CardRarity.COMMON).SetSeriesFromClassPackage();

    public KimizugiShiho() {
        super(DATA);
        this.Initialize(4, 0, 2, 3);
        this.SetUpgrade(1, 0, 0, 0);
        this.SetAffinity_Red(1, 1, 1);
        this.SetAffinity_Green(1);
        this.SetDrawPileCardPreview(GameUtilities::IsHindrance);
    }

    @Override
    public AbstractAttribute GetDamageInfo() {
        return super.GetDamageInfo().AddMultiplier(this.magicNumber);
    }

    @Override
    public void triggerOnManualDiscard() {
        super.triggerOnManualDiscard();
        GameActions.Bottom.GainForce(1);
    }

    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();
        GameActions.Bottom.GainForce(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        for(int i = 0; i < this.magicNumber; ++i) {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.DAGGER).SetVFXColor(Color.RED);
        }

        GameActions.Bottom.RetainPower(Affinity.Red);
        GameActions.Bottom.Draw(1).SetFilter(GameUtilities::IsHindrance, false);
    }
}