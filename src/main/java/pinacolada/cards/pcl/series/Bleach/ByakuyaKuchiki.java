package pinacolada.cards.pcl.series.Bleach;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.interfaces.delegates.ActionT3;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.ByakuyaBankai;
import pinacolada.effects.AttackEffects;
import pinacolada.resources.GR;
import pinacolada.stances.pcl.MightStance;
import pinacolada.stances.pcl.VelocityStance;
import pinacolada.utilities.PCLActions;

public class ByakuyaKuchiki extends PCLCard {
    public static final PCLCardData DATA = Register(ByakuyaKuchiki.class).SetAttack(3, CardRarity.RARE, PCLAttackType.Piercing).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new ByakuyaBankai(), false));


    public ByakuyaKuchiki() {
        super(DATA);

        Initialize(19, 15, 2);
        SetUpgrade(3, 3, 0);
        SetAffinity_Red(1, 0, 2);
        SetAffinity_Green(1, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.Callback(m, (enemy, __) -> ChooseAction(enemy));

        if (MightStance.IsActive() || VelocityStance.IsActive()) {
            PCLActions.Bottom.ChangeStance(NeutralStance.STANCE_ID);
            PCLActions.Bottom.MakeCardInDrawPile(new ByakuyaBankai());
            PCLActions.Last.ModifyAllInstances(uuid).AddCallback(PCLActions.Bottom::Exhaust);
        }
    }

    private void ChooseAction(AbstractMonster m) {
        PCLCard damage = GenerateInternal(CardType.ATTACK, this::DamageEffect, GR.PCL.Strings.Actions.GainAmount(magicNumber, GR.Tooltips.Might, true)).Build();
        PCLCard block = GenerateInternal(CardType.SKILL, this::BlockEffect, GR.PCL.Strings.Actions.GainAmount(magicNumber, GR.Tooltips.Velocity, true)).Build();

        CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        choices.addToTop(damage);
        choices.addToTop(block);

        Execute(choices, m);
    }

    private PCLCardBuilder GenerateInternal(AbstractCard.CardType type, ActionT3<PCLCard, AbstractPlayer, AbstractMonster> onUseAction, String description) {
        PCLCardBuilder builder = new PCLCardBuilder(ByakuyaKuchiki.DATA.ID);
        builder.SetText(name, description, "");
        builder.SetProperties(type, GR.Enums.Cards.THE_FOOL, AbstractCard.CardRarity.RARE, CardTarget.ENEMY);
        builder.SetOnUse(onUseAction);

        if (type.equals(CardType.ATTACK)) {
            builder.SetAttackType(PCLAttackType.Piercing, PCLCardTarget.Normal);
            builder.SetNumbers(damage, 0, magicNumber, 0, 1);
        } else {
            builder.SetNumbers(0, block, magicNumber, 0, 1);
        }

        return builder;
    }

    private void Execute(CardGroup group, AbstractMonster m) {
        PCLActions.Top.SelectFromPile(name, 1, group)
                .SetOptions(false, false)
                .AddCallback(m, (enemy, cards) ->
                {
                    AbstractCard card = cards.get(0);
                    card.applyPowers();
                    card.calculateCardDamage(enemy);
                    card.use(player, enemy);
                });
    }

    private void DamageEffect(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.SMASH);
        PCLActions.Bottom.GainMight(magicNumber);
    }

    private void BlockEffect(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainVelocity(magicNumber);
    }
}