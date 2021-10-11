package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.status.SearingBurn;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class HuTao extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HuTao.class).SetAttack(3, CardRarity.UNCOMMON, EYBAttackType.Piercing).SetSeriesFromClassPackage()
            .SetMaxCopies(2)
            .PostInitialize(data -> data.AddPreview(new SearingBurn(), false));

    public HuTao()
    {
        super(DATA);

        Initialize(2, 0, 4, 2);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Earth(1, 0, 0);
        SetAffinity_Dark(2, 0, 8);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        for (int i = 0; i < secondaryValue; i++)
        {
            GameActions.Bottom.MakeCardInHand(new SearingBurn())
                    .SetDuration(Settings.ACTION_DUR_XFAST, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE).SetVFXColor(Color.PURPLE);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SelectFromPile(name, magicNumber, p.drawPile, p.hand)
                .SetOptions(false, true)
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards)
                    {
                        GameActions.Bottom.Exhaust(card)
                                .ShowEffect(true, true)
                                .AddCallback(() -> GameActions.Bottom.MakeCardInHand(new SearingBurn()));
                    }

                    if (cards.size() > 0) {
                        GameActions.Bottom.GainEnergy(cards.size());
                    }
                });

    }
}