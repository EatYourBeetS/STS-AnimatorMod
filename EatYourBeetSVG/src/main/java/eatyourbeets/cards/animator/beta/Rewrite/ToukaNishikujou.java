package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class ToukaNishikujou extends AnimatorCard {
    public static final EYBCardData DATA = Register(ToukaNishikujou.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None);

    public ToukaNishikujou() {
        super(DATA);

        Initialize(0, 10, 8,9);
        SetUpgrade(0, 0, -2);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        ExhaustAndDamage();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block).AddCallback(__ -> {
            int numThrowingKnives = player.currentBlock / magicNumber;

            if (numThrowingKnives > 0) {
                GameActions.Bottom.CreateThrowingKnives(numThrowingKnives);
            }

            if (HasSynergy()) {
                ExhaustAndDamage();
            }

        });
    }

    private void ExhaustAndDamage()
    {
        GameActions.Bottom.Exhaust(this);
        GameActions.Bottom.VFX(new MindblastEffect(player.dialogX, player.dialogY, player.flipHorizontal), 0.1f);
        GameActions.Bottom.DealDamageToRandomEnemy(secondaryValue, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
    }
}