package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.animator.beta.special.ByakuyaBankai;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.resources.GR;
import eatyourbeets.stances.MightStance;
import eatyourbeets.stances.VelocityStance;
import eatyourbeets.utilities.GameActions;
//TODO Choose 1: Gain +M damage and Red, or Gain +M Block and Green
public class ByakuyaKuchiki extends AnimatorCard {
    public static final EYBCardData DATA = Register(ByakuyaKuchiki.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Piercing).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new ByakuyaBankai(), false));


    public ByakuyaKuchiki() {
        super(DATA);

        Initialize(23, 15, 0);
        SetUpgrade(3, 3, 0);
        SetAffinity_Red(2, 0, 2);
        SetAffinity_Green(2, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.Callback(m, (enemy, __) -> ChooseAction(enemy));

        if (MightStance.IsActive() || VelocityStance.IsActive()) {
            GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID);
            GameActions.Bottom.MakeCardInDrawPile(new ByakuyaBankai());
            GameActions.Last.ModifyAllInstances(uuid).AddCallback(GameActions.Bottom::Exhaust);
        }
    }

    private void ChooseAction(AbstractMonster m) {
        AnimatorCard damage = GenerateInternal(CardType.ATTACK, this::DamageEffect).Build();
        AnimatorCard block = GenerateInternal(CardType.SKILL, this::BlockEffect).Build();

        CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        choices.addToTop(damage);
        choices.addToTop(block);

        Execute(choices, m);
    }

    private AnimatorCardBuilder GenerateInternal(AbstractCard.CardType type, ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onUseAction) {
        AnimatorCardBuilder builder = new AnimatorCardBuilder(ByakuyaKuchiki.DATA.ID);
        builder.SetText(name, "", "");
        builder.SetProperties(type, GR.Enums.Cards.THE_ANIMATOR, AbstractCard.CardRarity.RARE, CardTarget.ENEMY);
        builder.SetOnUse(onUseAction);

        if (type.equals(CardType.ATTACK)) {
            builder.SetAttackType(EYBAttackType.Piercing, EYBCardTarget.Normal);
            builder.SetNumbers(damage, 0, 0, 0, 1);
        } else {
            builder.SetNumbers(0, block, 0, 0, 1);
        }

        return builder;
    }

    private void Execute(CardGroup group, AbstractMonster m) {
        GameActions.Top.SelectFromPile(name, 1, group)
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
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SMASH);
    }

    private void BlockEffect(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.GainBlock(block);
    }
}