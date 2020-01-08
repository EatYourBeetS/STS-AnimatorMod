package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameEffects;

public class Shigure extends AnimatorCard
{
    public static final String ID = Register(Shigure.class, EYBCardBadge.Exhaust);

    public Shigure()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(7, 0, 2, 3);
        SetUpgrade(2, 0, 1, 0);

        SetPiercing(true);
        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.StackPower(new SupportDamagePower(player, secondaryValue));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
        .SetPiercing(true, true)
        .SetDamageEffect(enemy ->
        {
            GameEffects.List.Add(new DaggerSprayEffect(AbstractDungeon.getMonsters().shouldFlipVfx()));
            GameActions.Top.ApplyPoison(player, enemy, magicNumber);
        });
    }
}