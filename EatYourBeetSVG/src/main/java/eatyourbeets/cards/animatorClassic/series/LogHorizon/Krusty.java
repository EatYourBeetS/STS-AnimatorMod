package eatyourbeets.cards.animatorClassic.series.LogHorizon;

import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Krusty extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Krusty.class).SetSeriesFromClassPackage().SetAttack(2, CardRarity.RARE, EYBAttackType.Normal);

    public Krusty()
    {
        super(DATA);

        Initialize(28, 0, 3, 3);
        SetUpgrade(1, 0, 1, 1);

        SetScaling(0, 0, 1);
        
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SMASH);
        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.HIGH));
        GameActions.Bottom.ModifyAllInstances(uuid, c ->
        {
            EYBCard card = (EYBCard) c;
            GameActions.Bottom.IncreaseScaling(c, Affinity.Red, secondaryValue);
            card.flash();
        });
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.PlayCard(this, player.hand, null)
        .SpendEnergy(true, true)
        .AddCondition(AbstractCard::hasEnoughEnergy);
    }
}