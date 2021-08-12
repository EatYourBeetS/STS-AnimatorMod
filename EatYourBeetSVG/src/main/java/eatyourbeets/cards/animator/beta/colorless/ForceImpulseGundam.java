package eatyourbeets.cards.animator.beta.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class ForceImpulseGundam extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ForceImpulseGundam.class).SetAttack(3, CardRarity.RARE).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.Gundam);

    public ForceImpulseGundam()
    {
        super(DATA);

        Initialize(15, 0, 5 );
        SetUpgrade(3, 0, 1 );

        SetAffinity_Red(2, 0, 4);
        SetAffinity_Light(1);

        SetCooldown(2, 0, this::OnCooldownCompleted);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY).SetVFX(true, false)
                .SetDamageEffect(enemy ->
                {
                    return GameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, Color.SKY)).duration * 0.1f;
                });
        GameActions.Bottom.Reload(name, cards ->
        {
            if (cards.size() > 0)
            {
                for (AbstractCard card : cards) {
                    GameUtilities.IncreaseDamage(this, magicNumber, true);
                    if (card.type == CardType.POWER || card.type == CardType.STATUS) {
                        GameUtilities.IncreaseDamage(this, magicNumber, true);
                        GameActions.Bottom.Exhaust(card);
                    }
                }
            }
        });
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
        GameActions.Bottom.Exhaust(this);
    }
}