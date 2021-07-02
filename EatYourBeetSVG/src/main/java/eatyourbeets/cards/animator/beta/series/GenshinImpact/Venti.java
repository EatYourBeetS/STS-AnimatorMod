package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Venti extends AnimatorCard {
    public static final EYBCardData DATA = Register(Venti.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None);
    private boolean canActivateEffect;

    public Venti() {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 2);

        SetEthereal(true);
        SetShapeshifter();
        SetSynergy(Synergies.GenshinImpact);

        this.canActivateEffect = true;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        AbstractOrb orb = new Aether();
        GameActions.Bottom.ChannelOrb(orb);

        this.canActivateEffect = true;
        GameActions.Bottom.Cycle(name, magicNumber).AddCallback(cards ->
        {
            for (AbstractCard card : cards) {
                if (card.type == CardType.SKILL) {
                    GameActions.Bottom.VFX(new WhirlwindEffect(), 0f);
                    orb.onStartOfTurn();
                    orb.onEndOfTurn();
                    this.canActivateEffect = false;
                }
            }

            if (cards.size() < magicNumber) {
                this.canActivateEffect = false;
            }
        });

        if (this.canActivateEffect && CombatStats.TryActivateLimited(cardID)) {
            GameActions.Top.PlayCopy(this, m);
        }

    }

}