package pinacolada.cards.pcl.colorless;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.tokens.AffinityToken;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class ForceImpulseGundam extends PCLCard
{
    public static final PCLCardData DATA = Register(ForceImpulseGundam.class).SetAttack(3, CardRarity.RARE).SetMultiformData(2, false).
            SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Gundam);
    private int bonusDamage = 0;

    public ForceImpulseGundam()
    {
        super(DATA);

        Initialize(18, 0, 5 );
        SetUpgrade(3, 0, 1 );

        SetAffinity_Red(1, 0, 4);
        SetAffinity_Silver(1);
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
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SLASH_HEAVY).forEach(d -> d.SetVFX(true, false)
                .SetDamageEffect(enemy ->
                        PCLGameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, Color.SKY)).duration * 0.1f));
        PCLActions.Bottom.Reload(name, cards ->
        {
            baseDamage -= bonusDamage;
            bonusDamage = 0;
            if (cards.size() > 0)
            {
                for (AbstractCard card : cards) {
                    PCLActions.Bottom.ModifyAllInstances(uuid, c -> ((ForceImpulseGundam)c).AddDamageBonus(magicNumber));
                    if (card.type == CardType.STATUS || card.type == (auxiliaryData.form == 1 ? CardType.SKILL :CardType.POWER)) {
                        PCLActions.Bottom.ModifyAllInstances(uuid, c -> ((ForceImpulseGundam)c).AddDamageBonus(magicNumber));
                        PCLActions.Bottom.Exhaust(card);
                    }
                }
            }
        });

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        PCLActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(PCLAffinity.Red, upgraded));
        PCLActions.Bottom.Exhaust(this);
    }

    private void AddDamageBonus(int amount)
    {
        bonusDamage += amount;
        baseDamage += amount;
    }
}