package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.interfaces.delegates.ActionT3;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class ByakuyaBankai extends PCLCard {
    public static final PCLCardData DATA = Register(ByakuyaBankai.class).SetAttack(-1, CardRarity.SPECIAL, PCLAttackType.Ranged, PCLCardTarget.AoE).SetSeries(CardSeries.Bleach);

    public ByakuyaBankai() {
        super(DATA);

        Initialize(6, 5, 1);
        SetUpgrade(2, 2, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 0);

        SetUnique(true, -1);
        SetExhaust(true);
        SetMultiDamage(true);
    }

    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();
        PCLActions.Bottom.MoveCard(this, player.exhaustPile, player.discardPile)
                .ShowEffect(false, false);
        if (this.canUpgrade()) {
            this.upgrade();
            this.flash();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        int stacks = PCLGameUtilities.UseXCostEnergy(this);

        for (int i = 0; i < stacks; i++) {
            PCLActions.Bottom.Callback(m, (enemy, __) ->
                    ChooseAction(enemy)
            );
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
        PCLCardBuilder builder = new PCLCardBuilder(ByakuyaBankai.DATA.ID);
        builder.SetText(name, description, "");
        builder.SetProperties(type, GR.Enums.Cards.THE_FOOL, AbstractCard.CardRarity.RARE, CardTarget.ENEMY);
        builder.SetOnUse(onUseAction);

        if (type.equals(CardType.ATTACK)) {
            builder.SetAttackType(PCLAttackType.Ranged, PCLCardTarget.AoE);
            builder.SetNumbers(damage, 0, magicNumber, 0, 1);
        } else {
            builder.SetNumbers(0, block, magicNumber, 0, 1);
        }

        return builder;
    }

    private void Execute(CardGroup group, AbstractMonster m) {
        PCLActions.Top.SelectFromPile(name, 1, group)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    AbstractCard card = cards.get(0);
                    card.applyPowers();
                    card.calculateCardDamage(m);
                    card.use(player, m);
                });
    }

    private void DamageEffect(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        PCLActions.Bottom.VFX(new BorderLongFlashEffect(Color.WHITE));
        PCLActions.Bottom.VFX(new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.WHITE, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.75f);

        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HEAVY);
        PCLActions.Bottom.GainMight(magicNumber);
    }

    private void BlockEffect(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainVelocity(magicNumber);
    }
}