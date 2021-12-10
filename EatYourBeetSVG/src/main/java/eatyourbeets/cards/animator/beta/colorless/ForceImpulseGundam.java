package eatyourbeets.cards.animator.beta.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class ForceImpulseGundam extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ForceImpulseGundam.class).SetAttack(3, CardRarity.RARE).SetMultiformData(2, false).
            SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Gundam);
    private int bonusDamage = 0;

    public ForceImpulseGundam()
    {
        super(DATA);

        Initialize(15, 0, 5 );
        SetUpgrade(3, 0, 1 );

        SetAffinity_Red(1, 0, 4);
        SetAffinity_Silver(2);
        SetAffinity_Light(1);

        SetCooldown(2, 0, this::OnCooldownCompleted);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
        }
        else {
            this.cardText.OverrideDescription(null, true);
        }
        return super.SetForm(form, timesUpgraded);
    };

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY).forEach(d -> d.SetVFX(true, false)
                .SetDamageEffect(enemy ->
                        GameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, Color.SKY)).duration * 0.1f));
        GameActions.Bottom.Reload(name, cards ->
        {
            if (cards.size() > 0)
            {
                for (AbstractCard card : cards) {
                    GameActions.Bottom.ModifyAllInstances(uuid, c -> ((ForceImpulseGundam)c).AddDamageBonus(magicNumber));
                    if (card.type == CardType.STATUS || card.type == (auxiliaryData.form == 1 ? CardType.SKILL :CardType.POWER)) {
                        GameActions.Bottom.ModifyAllInstances(uuid, c -> ((ForceImpulseGundam)c).AddDamageBonus(magicNumber));
                        GameActions.Bottom.Exhaust(card);
                    }
                }
            }
        });
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(Affinity.Red, upgraded));
        GameActions.Bottom.Exhaust(this);
    }

    private void AddDamageBonus(int amount)
    {
        bonusDamage += amount;
        baseDamage += amount;
    }
}